import { For, Show } from 'solid-js';
import Query from '../base/query/Query';
import { saveFetch } from '../../lib/safeFetch';
import { type SearchResult, searchResultsSchema } from '../../lib/schemas';
import { timed } from '../../lib/timed';

function SearchResultContainer(props: SearchResult) {
    return (
        <div class="m-4 max-h-48 w-1/2 min-w-xl rounded-lg bg-neutral-500/80 p-4 shadow-2xl">
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

                    <div class="center">
                        <div class="flex items-center justify-between rounded-xl bg-neutral-500/80 p-2">
                            <Show
                                when={page > 0}
                                fallback={
                                    <a href="#" class="button w-40 text-center">
                                        Previous
                                    </a>
                                }
                            >
                                <a href={'/search?' + encodeParams(page - 1)} class="button w-40 text-center">
                                    Previous
                                </a>
                            </Show>
                            <p class="mr-4 ml-4">Search took {time}ms</p>
                            <a href={'/search?' + encodeParams(page + 1)} class="button w-40 text-center">
                                Next
                            </a>
                        </div>
                    </div>
                </>
            )}
        </Query>
    );
}
