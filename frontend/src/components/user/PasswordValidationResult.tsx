import type { JSX } from 'solid-js';
import type { PasswordResult } from '../../lib/password';

export default function (props: PasswordResult & { children?: JSX.Element }) {
    return (
        <>
            <p style={{ color: props.missingLength ? 'red' : 'green' }}>Password must contain at least 8 characters</p>
            <p style={{ color: props.missingDigits ? 'red' : 'green' }}>Password must contain at least one digit</p>
            <p style={{ color: props.missingUpperCase ? 'red' : 'green' }}>Password must contain at least one uppercase letter</p>
            <p style={{ color: props.missingLowerCase ? 'red' : 'green' }}>Password must contain at least one lowercase letter</p>
            <p style={{ color: props.missingSpecialChar ? 'red' : 'green' }}>Password must contain at least one special character</p>
            {props.children}
        </>
    );
}
