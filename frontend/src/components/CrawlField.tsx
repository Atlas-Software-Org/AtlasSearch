import { createSignal, useContext } from 'solid-js';
import { crawlResultSchema, type CrawlResult } from '../lib/schemas';
import Overlay from './base/generic/Overlay';
import Loading, { LoadingContext } from './base/loading/Loading';
import { timed } from '../lib/timed';
import { saveFetch } from '../lib/safeFetch';
import { withQuery } from './base/query/Query';

export function ShowCrawlResult(props: { status?: CrawlResult['status']; url: string; reset: () => void }) {
    return (
        <Overlay visible={!!props.status} reset={props.reset}>
            <div class="field">
                <p>
                    Crawler returned {props.status} for <a href={props.url}>{props.url}</a>.
                </p>
                <div class="center">
                    <button class="button" onclick={props.reset}>
                        Close
                    </button>
                </div>
            </div>
        </Overlay>
    );
}

function Wrapped() {
    const loading = useContext(LoadingContext);
    const [url, setUrl] = createSignal('');
    const [crawlResult, setCrawlResult] = createSignal<CrawlResult | null>(null);

    const submit = () => {
        withQuery(
            () => timed(() => saveFetch('/api/v1/crawl?l=' + encodeURIComponent(url()), {}, crawlResultSchema), 'v1/crawl'),
            loading,
            true,
            ([status]) => {
                setCrawlResult(status);
            }
        );
    };

    return (
        <>
            <div class="center h-full pt-8">
                <div class="field w-1/2 max-sm:w-4/5">
                    <div class="pb-2 text-red-400">
                        <p>You need to be logged in to crawl web pages!</p>
                        <p>Web pages need to start with http:// or https://</p>
                    </div>

                    <form
                        onSubmit={(e) => {
                            e.preventDefault();
                            submit();
                        }}
                    >
                        <table>
                            <tbody>
                                <tr>
                                    <td class="pr-2 text-nowrap">URL</td>
                                    <td class="w-full">
                                        <input type="text" class="input w-full" onChange={(e) => setUrl(e.target.value)} value={url()} required />
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <div class="center">
                            <button class="button" type="submit">
                                Crawl
                            </button>
                        </div>
                    </form>
                </div>
            </div>
            <ShowCrawlResult status={crawlResult()?.status} url={url()} reset={() => setCrawlResult(null)} />
        </>
    );
}

export default function () {
    return (
        <Loading initial={false} inline={false}>
            <Wrapped />
        </Loading>
    );
}
