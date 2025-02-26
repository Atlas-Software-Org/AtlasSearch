import { createSignal, Match, Switch, useContext } from 'solid-js';
import { saveFetch } from '../../lib/safeFetch';
import { userSchema, type User } from '../../lib/schemas';
import { timed } from '../../lib/timed';
import Query, { withQuery } from '../base/query/Query';
import ContinueBox from '../base/generic/ContinueBox';
import { QueryContext } from '../base/query/QueryController';
import { LoadingContext } from '../base/loading/Loading';

export interface Props {
    username: string;
}

function UserButtons(props: User) {
    const query = useContext(QueryContext);
    const loading = useContext(LoadingContext);

    const deleteUser = () => {
        withQuery(
            () => timed(() => saveFetch('/api/v1/admin/deleteUser?u=' + encodeURIComponent(props.username)), 'deleteUser'),
            loading,
            true,
            () => query.refetch('userList')
        );
    };

    const changeUserMeta = (isAdministrator: boolean, isPremiumUser: boolean) => {
        withQuery(
            () =>
                timed(
                    () =>
                        saveFetch('/api/v1/admin/changeUserMetadata', {
                            method: 'POST',
                            body: JSON.stringify({ username: props.username, isAdministrator, isPremiumUser }),
                        }),
                    'changeUserMetadata'
                ),
            loading,
            true,
            () => query.refetch('adminUser')
        );
    };

    const [deleteContinueVisible, setDeleteContinueVisible] = createSignal(false);

    return (
        <>
            <button
                class="button w-[80%]"
                onClick={() => {
                    setDeleteContinueVisible(true);
                }}
            >
                Delete user
            </button>
            <Switch>
                <Match when={props.isPremiumUser}>
                    <button class="button w-[80%]" onClick={() => changeUserMeta(props.isAdministrator, false)}>
                        Remove premium
                    </button>
                </Match>
                <Match when={!props.isPremiumUser}>
                    <button class="button w-[80%]" onClick={() => changeUserMeta(props.isAdministrator, true)}>
                        Set premium
                    </button>
                </Match>
            </Switch>

            <Switch>
                <Match when={props.isAdministrator}>
                    <button class="button w-[80%]" onClick={() => changeUserMeta(false, props.isPremiumUser)}>
                        Remove admin
                    </button>
                </Match>
                <Match when={!props.isAdministrator}>
                    <button class="button w-[80%]" onClick={() => changeUserMeta(true, props.isPremiumUser)}>
                        Set admin
                    </button>
                </Match>
            </Switch>
            <ContinueBox visible={deleteContinueVisible()} resetCallback={() => setDeleteContinueVisible(false)} continueCallback={deleteUser} cancelCallback={() => {}}>
                <p>Are you sure you want to delete this user?</p>
            </ContinueBox>
        </>
    );
}

export default function (props: Props) {
    return (
        <Query f={() => timed(() => saveFetch('/api/v1/admin/user?u=' + encodeURIComponent(props.username), {}, userSchema), 'adminUser')} queryKey="adminUser">
            {([user]) => (
                <>
                    <div class="center flex-col">
                        <h1 class="text-2xl">{user.username}</h1>
                        <img src={user.profilePictureUrl} class="h-24 w-24 rounded-full" />

                        <UserButtons {...user} />
                    </div>
                </>
            )}
        </Query>
    );
}
