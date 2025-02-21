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
            <div class="center hover:cursor-pointer" onClick={() => setVisible(!visible())}>
                <p>Sign Up</p>
                <img class="ml-1 h-8 w-8 rounded-full bg-zinc-900" />
            </div>
            <Overlay visible={visible()}>
                <div class="field">
                    <LoginOrRegisterField />;
                </div>
            </Overlay>
        </>
    );
}

function LoggedIn(props: User) {
    const [visible, setVisible] = createSignal(false);

    return (
        <>
            <div class="center hover:cursor-pointer" onClick={() => setVisible(!visible())}>
                <p>{props.username}</p>
                <img class="ml-1 h-8 w-8 rounded-full bg-zinc-900" src={props.profilePictureUrl} />
            </div>
            <Overlay visible={visible()}>
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
        <div class="center absolute right-32">
            <UserProvider inlineLoading={true}>
                <Wrapped />
            </UserProvider>
        </div>
    );
}
