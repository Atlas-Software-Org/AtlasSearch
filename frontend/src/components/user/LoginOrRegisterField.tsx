import { createSignal, Show, useContext } from 'solid-js';
import { passwordOk, createPasswordValidation } from '../../lib/password';
import { QueryContext } from '../base/query/QueryController';
import { withQuery } from '../base/query/Query';
import Loading, { LoadingContext } from '../base/loading/Loading';
import { timed } from '../../lib/timed';
import PasswordValidationResult from './PasswordValidationResult';
import { saveFetch } from '../../lib/safeFetch';

function Wrapped() {
    const query = useContext(QueryContext);
    const loading = useContext(LoadingContext);
    const [username, setUsername] = createSignal('');
    const [password, setPassword] = createSignal('');
    const [creationMode, setCreationMode] = createSignal(false);

    const passwordResult = createPasswordValidation(password);

    const submit = () => {
        const mode = creationMode();
        const url = mode ? '/api/v1/register' : '/api/v1/login';
        withQuery(
            () => timed(() => saveFetch(url, { method: 'POST', body: JSON.stringify({ username: username(), password: password() }) }), mode ? 'register' : 'login'),
            loading,
            true,
            () => {
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
                        <td class="pr-2 text-nowrap">Password</td>
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
