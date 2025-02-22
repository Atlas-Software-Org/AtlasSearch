import { createSignal } from 'solid-js';

export interface Props {
    initialQuery?: string;
    small?: boolean;
}

export default function (props: Props) {
    const [query, setQuery] = createSignal(props.initialQuery ?? '');
    const submit = () => {
        const q = query();
        if (!q) {
            alert('Please enter a search query');
            return;
        }

        location.href = `/search?q=${q}`;
    };

    return (
        <form
            class="w-full"
            onSubmit={(e) => {
                e.preventDefault();
                submit();
            }}
        >
            <div class={props.small ? '' : 'center'}>
                <input
                    type="text"
                    class={'bg-neutral-500 select-auto ' + (props.small ? 'w-[25%] rounded-sm p-1 text-xl' : 'w-2xl rounded-xl bg-neutral-500/75! p-2 text-3xl')}
                    placeholder="Search with Atlas Search"
                    value={query()}
                    onChange={(e) => setQuery(e.currentTarget.value)}
                    required
                />
                <button class="button" type="submit">
                    Go!
                </button>
            </div>
        </form>
    );
}
