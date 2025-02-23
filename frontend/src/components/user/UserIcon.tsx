import { createSignal, Show, useContext } from 'solid-js';
import Overlay from '../base/generic/Overlay';
import LoginOrRegisterField from './LoginOrRegisterField';
import UserProvider, { UserContext } from './UserProvider';
import type { User } from '../../lib/schemas';
import UserSettingsField from './UserSettingsField';

function NotLoggedIn() {
    const [visible, setVisible] = createSignal(false);
    return (
        <>
            <div class="center rounded-xl p-2 hover:cursor-pointer hover:bg-neutral-500" onClick={() => setVisible(!visible())}>
                <p>Sign Up</p>
                <img class="ml-1 h-8 w-8 rounded-full bg-neutral-900" />
            </div>
            <Overlay visible={visible()} reset={() => setVisible(false)}>
                <div class="field">
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

function Wrapped() {
    const provider = useContext(UserContext);
    return (
        <Show when={provider.user} fallback={<NotLoggedIn />}>
            <LoggedIn {...provider.user!} />
        </Show>
    );
}

export default function () {
    return (
        <div class="absolute right-4 sm:right-32">
            <UserProvider inlineLoading={true}>
                <Wrapped />
            </UserProvider>
        </div>
    );
}
