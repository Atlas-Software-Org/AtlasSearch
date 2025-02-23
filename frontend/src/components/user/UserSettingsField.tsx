import { createSignal } from 'solid-js';
import UploadProfilePicture from './UploadProfilePicture';
import Overlay from '../base/generic/Overlay';
import PasswordChangeField from './PasswordChangeField';

export default function () {
    const [changePassword, setChangePassword] = createSignal(false);
    return (
        <div>
            <button class="button" onClick={() => setChangePassword(true)}>
                Change Password
            </button>
            <UploadProfilePicture />

            <Overlay visible={changePassword()} reset={() => setChangePassword(false)}>
                <div class="field">
                    <PasswordChangeField />
                </div>
            </Overlay>
        </div>
    );
}
