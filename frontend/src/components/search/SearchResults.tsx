import { For, Show } from 'solid-js';
import Query from '../base/query/Query';
import { saveFetch } from '../../lib/safeFetch';
import { type SearchResult, searchResultsSchema } from '../../lib/schemas';
import { timed } from '../../lib/timed';

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
    const q = new URLSearchParams(window.location.search).get('q') || 'test';
    const encodeParams = (page: number) => new URLSearchParams({ q, page: page.toString() }).toString();

    const page = parseInt(new URLSearchParams(window.location.search).get('page') ?? '0');
    return (
        <Query f={() => timed(() => saveFetch('/api/v1/search?' + encodeParams(page), {}, searchResultsSchema), 'search')}>
            {([results, time]) => (
                <>
                    <For each={results}>{(result) => <SearchResultContainer {...result} />}</For>
                    <p>Search took {time}ms</p>
                    <Show when={page > 0}>
                        <a href={'/search?' + encodeParams(page - 1)}>Previous</a>
                    </Show>
                    <a href={'/search?' + encodeParams(page + 1)}>Next</a>
                </>
            )}
        </Query>
    );
}
