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
            class="w-full flex justify-center z-5"
            onSubmit={(e) => {
                e.preventDefault();
                submit();
            }}
        >
            <div
                class={
                    'flex items-center gap-2 bg-neutral-800/70 backdrop-blur-md shadow-lg rounded-2xl px-4 py-2 transition-all duration-300 ' +
                    (props.small
                        ? 'w-[300px]'
                        : 'w-full max-w-3xl max-sm:flex-col max-sm:gap-3')
                }
            >
                <input
                    type="text"
                    class="flex-1 rounded-xl bg-neutral-700 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-cyan-400 px-4 py-2 text-xl transition-all w-full"
                    placeholder="Search"
                    value={query()}
                    onChange={(e) => setQuery(e.currentTarget.value)}
                    required
                />
                <button
                    type="submit"
                    class="bg-cyan-600 text-white px-4 py-2 rounded-xl hover:bg-cyan-500 active:scale-95 transition-all"
                >
                    Go!
                </button>
            </div>
        </form>
    );
}
