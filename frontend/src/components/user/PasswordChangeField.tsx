import { createSignal, Show, useContext } from 'solid-js';
import Loading, { LoadingContext } from '../base/loading/Loading';
import { QueryContext } from '../base/query/QueryController';
import { createPasswordValidation, passwordOk } from '../../lib/password';
import PasswordValidationResult from './PasswordValidationResult';
import { withQuery } from '../base/query/Query';
import { timed } from '../../lib/timed';
import { saveFetch } from '../../lib/safeFetch';

function Wrapped() {
    const query = useContext(QueryContext);
    const loading = useContext(LoadingContext);
    const [oldPassword, setOldPassword] = createSignal('');
    const [newPassword, setNewPassword] = createSignal('');
    const [confirmPassword, setConfirmPassword] = createSignal('');

    const passwordResult = createPasswordValidation(newPassword);

    const submit = () => {
        withQuery(
            () =>
                timed(
                    () => saveFetch('/api/v1/changePassword', { method: 'POST', body: JSON.stringify({ oldPassword: oldPassword(), newPassword: newPassword() }) }),
                    'changePassword'
                ),
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
                        <td class="pr-2 text-nowrap">Old Password</td>
                        <td class="w-full">
                            <input type="password" class="input w-full" onChange={(e) => setOldPassword(e.target.value)} value={oldPassword()} required />
                        </td>
                    </tr>
                    <tr>
                        <td class="pr-2 text-nowrap">New Password</td>
                        <td class="w-full">
                            <input type="password" class="input w-full" onInput={(e) => setNewPassword(e.target.value)} value={newPassword()} required />
                        </td>
                    </tr>
                    <tr>
                        <td class="pr-2 text-nowrap">Confirm Password</td>
                        <td class="w-full">
                            <input type="password" class="input w-full" onInput={(e) => setConfirmPassword(e.target.value)} value={confirmPassword()} required />
                        </td>
                    </tr>
                </tbody>
            </table>
            <br />

            <PasswordValidationResult {...passwordResult()}>
                <Show when={newPassword() != confirmPassword()}>
                    <p style={{ color: 'red' }}>Passwords do not match</p>
                </Show>
            </PasswordValidationResult>

            <div class="center">
                <button class="button" type="submit" disabled={!passwordOk(passwordResult()) || newPassword() != confirmPassword()}>
                    Update Password
                </button>
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
