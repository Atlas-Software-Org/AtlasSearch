export default function () {
    let name = getComputedStyle(document.documentElement).getPropertyValue('--bg-name').trim();
    let username = getComputedStyle(document.documentElement).getPropertyValue('--bg-username').trim();

    return (
        <div class="rounded-xl bg-neutral-700/75 p-2">
            <p>
                Photo by {name} on{' '}
                <a class="text-blue-400 underline" href={`https://unsplash.com/@${username}?utm_source=thorax&utm_medium=referral`}>
                    Unsplash
                </a>
            </p>
        </div>
    );
}
