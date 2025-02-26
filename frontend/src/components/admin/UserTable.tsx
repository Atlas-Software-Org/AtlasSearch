import { createSignal, For } from 'solid-js';
import { saveFetch } from '../../lib/safeFetch';
import { userListSchema, type PartialUser } from '../../lib/schemas';
import { timed } from '../../lib/timed';
import Query from '../base/query/Query';
import Overlay from '../base/generic/Overlay';
import User from './User';
import QueryController from '../base/query/QueryController';

export function UserEntry(props: PartialUser) {
    const [visible, setVisible] = createSignal(false);
    return (
        <>
            <tr>
                <td class="pr-2 pb-2">
                    <img src={props.profilePictureUrl} class="h-8 w-8 rounded-full" />
                </td>
                <td class="pb-2 underline hover:cursor-pointer" onClick={() => setVisible(true)}>
                    {props.username}
                </td>
            </tr>
            <Overlay visible={visible()} reset={() => setVisible(false)}>
                <div class="field">
                    <User username={props.username} />
                </div>
            </Overlay>
        </>
    );
}

function Wrapped() {
    return (
        <div class="center">
            <Query f={() => timed(() => saveFetch('/api/v1/admin/userList', {}, userListSchema), 'v1/admin/userList')} queryKey="userList">
                {([users]) => (
                    <div class="field sm:min-w-xl">
                        <table class="w-full p-2">
                            <thead class="">
                                <tr class="border-b">
                                    <th class="sm:text-2xl" colSpan={2}>
                                        <b>Username</b>
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <For each={users}>{(entry) => <UserEntry {...entry} />}</For>
                            </tbody>
                        </table>
                    </div>
                )}
            </Query>
        </div>
    );
}

export default function () {
    return (
        <QueryController>
            <Wrapped />
        </QueryController>
    );
}
