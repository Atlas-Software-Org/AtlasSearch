import { Show } from 'solid-js';
import SearchBar from './SearchBar';

export default function () {
    const q = new URLSearchParams(window.location.search).get('q');

    return (
        <Show when={q} fallback={<p>No search query</p>}>
            <SearchBar initialQuery={q!} small={true} />
        </Show>
    );
}
