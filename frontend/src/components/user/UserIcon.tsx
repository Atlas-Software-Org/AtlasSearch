import { createEffect, createSignal, Show, useContext } from 'solid-js';
import Overlay from '../base/generic/Overlay';
import LoginOrRegisterField from './LoginOrRegisterField';
import UserProvider, { UserContext } from './UserProvider';
import type { User } from '../../lib/schemas';
import UserSettingsField from './UserSettingsField';

export interface Props {
    loginRequired?: boolean;
    premiumRequired?: boolean;
    administratorRequired?: boolean;
}

function NotLoggedIn() {
    const [visible, setVisible] = createSignal(false);
    return (
        <>
            <div class="center rounded-xl p-2 hover:cursor-pointer hover:bg-neutral-500" onClick={() => setVisible(!visible())}>
                <p>Sign Up</p>
                <img class="ml-1 h-8 w-8 rounded-full bg-neutral-900" />
            </div>
            <Overlay visible={visible()} reset={() => setVisible(false)}>
                <div class="field ">
                    <LoginOrRegisterField />
                </div>
            </Overlay>
        </>
    );
}

function LoggedIn(props: User) {
    const [visible, setVisible] = createSignal(false);

    return (
        <>
            <div class="center rounded-xl p-2 hover:cursor-pointer hover:bg-neutral-500" onClick={() => setVisible(!visible())}>
                <p>{props.username}</p>
                <img class="ml-1 h-8 w-8 rounded-full bg-neutral-900" src={props.profilePictureUrl} />
            </div>
            <Overlay visible={visible()} reset={() => setVisible(false)}>
                <div class="field">
                    <UserSettingsField />
                </div>
            </Overlay>
        </>
    );
}

function Wrapped(props: Props) {
    const provider = useContext(UserContext);

    const [loginRequired, setLoginRequired] = createSignal(false);
    const [premiumRequired, setPremiumRequired] = createSignal(false);
    const [administratorRequired, setAdministratorRequired] = createSignal(false);

    createEffect(() => {
        if (provider.user) {
            if (props.premiumRequired && !provider.user.isPremiumUser) {
                setPremiumRequired(true);
            } else if (props.administratorRequired && !provider.user.isAdministrator) {
                setAdministratorRequired(true);
            }
        } else if (props.loginRequired) {
            setLoginRequired(true);
        }
    });

    return (
        <>
            <Show when={provider.user} fallback={<NotLoggedIn />}>
                <LoggedIn {...provider.user!} />
            </Show>
            <Overlay visible={loginRequired()} reset={() => {}}>
                <div class="field">
                    <LoginOrRegisterField>
                        <p>You must be logged in to view this page</p>
                    </LoginOrRegisterField>
                </div>
            </Overlay>
            <Overlay visible={premiumRequired()} reset={() => setPremiumRequired(false)}>
                <div class="field">
                    <p>You must be a premium user to view this page</p>
                </div>
            </Overlay>
            <Overlay visible={administratorRequired()} reset={() => setAdministratorRequired(false)}>
                <div class="field">
                    <p>You must be an administrator to view this page</p>
                </div>
            </Overlay>
        </>
    );
}

export default function (props: Props) {
    return (
        <div class="absolute right-4 sm:right-32">
            <UserProvider inlineLoading={true}>
                <Wrapped {...props} />
            </UserProvider>
        </div>
    );
}
