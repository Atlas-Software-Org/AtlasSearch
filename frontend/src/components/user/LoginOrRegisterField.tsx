import { createEffect, createSignal, Show, useContext } from 'solid-js';
import { validatePassword, passwordOk, type PasswordResult, defaultPasswordResult } from '../../password';
import { QueryContext } from '../base/query/QueryController';
import { withQuery } from '../base/query/Query';
import Loading, { LoadingContext } from '../base/loading/Loading';
import { timed } from '../../timed';

function PasswordValidationResult(props: PasswordResult) {
    return (
        <>
            <p style={{ color: props.missingLength ? 'red' : 'green' }}>Password must contain at least 8 characters</p>
            <p style={{ color: props.missingDigits ? 'red' : 'green' }}>Password must contain at least one digit</p>
            <p style={{ color: props.missingUpperCase ? 'red' : 'green' }}>Password must contain at least one uppercase letter</p>
            <p style={{ color: props.missingLowerCase ? 'red' : 'green' }}>Password must contain at least one lowercase letter</p>
            <p style={{ color: props.missingSpecialChar ? 'red' : 'green' }}>Password must contain at least one special character</p>
        </>
    );
}

function Wrapped() {
    const query = useContext(QueryContext);
    const loading = useContext(LoadingContext);
    const [username, setUsername] = createSignal('');
    const [password, setPassword] = createSignal('');
    const [creationMode, setCreationMode] = createSignal(false);

    const [passwordResult, setPasswordResult] = createSignal<PasswordResult>(defaultPasswordResult);
    createEffect(() => {
        setPasswordResult(validatePassword(password()));
    });

    const submit = () => {
        const mode = creationMode();
        const url = mode ? '/api/v1/register' : '/api/v1/login';
        withQuery(
            () => timed(() => fetch(url, { method: 'POST', body: JSON.stringify({ username: username(), password: password() }) }), mode ? 'register' : 'login'),
            loading,
            true,
            (t) => {
                query.refetch('user');
            }
        );
    };

    return (
        <form
            on:submit={(e) => {
                e.preventDefault();
                submit();
            }}
        >
            <table>
                <tbody>
                    <tr>
                        <td class="pr-2 text-nowrap">Username</td>
                        <td class="w-full">
                            <input type="text" class="input w-full" onChange={(e) => setUsername(e.target.value)} value={username()} required />
                        </td>
                    </tr>
                    <tr>
                        <td class="pr-2 text-nowrap"> Password</td>
                        <td class="w-full">
                            <input type="password" class="input w-full" onInput={(e) => setPassword(e.target.value)} value={password()} required />
                        </td>
                    </tr>
                </tbody>
            </table>
            <br />

            <Show when={creationMode()}>
                <PasswordValidationResult {...passwordResult()} />
            </Show>

            <div class="center">
                <Show
                    when={creationMode()}
                    fallback={
                        <button class="button" type="submit">
                            Login
                        </button>
                    }
                >
                    <button class="button" type="submit" disabled={!passwordOk(passwordResult())}>
                        Register
                    </button>
                </Show>

                <Show when={!creationMode()}>
                    <button
                        onClick={(e) => {
                            e.preventDefault();
                            setCreationMode(true);
                        }}
                    >
                        I don't have an account
                    </button>
                </Show>
            </div>
        </form>
    );
}

export default function () {
    return (
        <Loading initial={false} inline={false}>
            <Wrapped />
        </Loading>
    );
}
