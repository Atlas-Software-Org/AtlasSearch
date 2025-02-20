import { For, Show } from 'solid-js';
import Query from '../base/query/Query';
import { fetchAndValidate } from '../../validatedFetch';
import { type SearchResult, searchResultsSchema } from '../../schemas';
import { timed } from '../../timed';

function SearchResultContainer(props: SearchResult) {
    return (
        <div class="m-4 max-h-48 w-1/2 min-w-xl rounded-lg bg-zinc-500 p-4 opacity-75 shadow-2xl">
            <div>
                <a href={'/redirect?l=' + props.link}>
                    <Show when={props.title} fallback={<>{props.link}</>}>
                        {props.title}
                    </Show>
                </a>
            </div>

            <small>
                <a href={'/redirect?l=' + props.link}>{props.link}</a>
            </small>
            <p>
                <Show when={props.description} fallback={<>{props.shortText}</>}>
                    {props.description}
                </Show>
            </p>
        </div>
    );
}

export default function () {
    const q = new URLSearchParams(window.location.search).get('q');

    return (
        <Show when={q} fallback={<p>No search query</p>}>
            <Query f={() => timed(() => fetchAndValidate(searchResultsSchema, () => fetch('/api/v1/search?q=' + encodeURIComponent(q!))), 'search')}>
                {([results, time]) => (
                    <>
                        <For each={results}>{(result) => <SearchResultContainer {...result} />}</For>
                        <p>Search took {time}ms</p>
                    </>
                )}
            </Query>
        </Show>
    );
}
