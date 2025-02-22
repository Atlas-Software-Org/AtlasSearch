import { Show } from 'solid-js';
import SearchBar from './SearchBar';

export interface Props {
    small?: boolean;
}

export default function (props: Props) {
    const q = new URLSearchParams(window.location.search).get('q');

    return (
        <Show when={q} fallback={<p>No search query</p>}>
            <SearchBar initialQuery={q!} small={props.small ?? true} />
        </Show>
    );
}
