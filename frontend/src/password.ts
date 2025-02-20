export interface PasswordResult {
    missingLength: boolean;
    missingDigits: boolean;
    missingUpperCase: boolean;
    missingLowerCase: boolean;
    missingSpecialChar: boolean;
}

export const defaultPasswordResult: PasswordResult = {
    missingLength: true,
    missingDigits: true,
    missingUpperCase: true,
    missingLowerCase: true,
    missingSpecialChar: true,
};

export function validatePassword(password: string): PasswordResult {
    const result: PasswordResult = {
        missingLength: password.length < 8,
        missingDigits: password.match(/.*\d.*/) === null,
        missingUpperCase: password.match(/.*[A-Z].*/) === null,
        missingLowerCase: password.match(/.*[a-z].*/) === null,
        missingSpecialChar: password.match(/.*[^a-zA-Z0-9].*/) === null,
    };

    return result;
}

export function passwordOk(result: PasswordResult): boolean {
    return !(result.missingLength || result.missingDigits || result.missingUpperCase || result.missingLowerCase || result.missingSpecialChar);
}
