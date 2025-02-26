import { createSignal, For } from 'solid-js';
import { saveFetch } from '../../lib/safeFetch';
import { timed } from '../../lib/timed';
import Query from '../base/query/Query';
import { crawlHistorySchema, type CrawlHistoryEntry } from '../../lib/schemas';
import Overlay from '../base/generic/Overlay';
import User from './User';

function statusToColor(status: CrawlHistoryEntry['status']) {
    switch (status) {
        case 'INSERTED':
            return 'green';
        case 'UPDATED':
            return 'blue';
        case 'REJECTED':
            return 'yellow';
        case 'FAILED':
            return 'red';
        default:
            return 'gray';
    }
}

function CrawlHistoryTableEntry(props: CrawlHistoryEntry) {
    const [visible, setVisible] = createSignal(false);
    return (
        <>
            <tr>
                <td class="pr-2 underline hover:cursor-pointer" onClick={() => setVisible(true)}>
                    {props.username}
                </td>
                <td class="pr-2">
                    <a href="{props.url}" class="text-blue-400">
                        {props.url}
                    </a>
                </td>
                <td style={{ color: statusToColor(props.status) }} class="pr-2">
                    {props.status}
                </td>
                <td>{new Date(props.timestamp).toLocaleString()}</td>
            </tr>
            <Overlay visible={visible()} reset={() => setVisible(false)}>
                <div class="field">
                    <User username={props.username} />
                </div>
            </Overlay>
        </>
    );
}

export default function () {
    return (
        <div class="center">
            <Query f={() => timed(() => saveFetch('/api/v1/admin/crawlHistory', {}, crawlHistorySchema), 'crawlHistory')}>
                {([history]) => (
                    <div class="field">
                        <table class="w-full p-2">
                            <thead class="">
                                <tr class="border-b">
                                    <th class="sm:text-2xl">
                                        <b> Username</b>
                                    </th>
                                    <th class="sm:text-2xl">
                                        <b>URL</b>
                                    </th>
                                    <th class="sm:text-2xl">
                                        <b>Status</b>
                                    </th>
                                    <th class="sm:text-2xl">
                                        <b>Timestamp</b>
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <For each={history}>{(entry) => <CrawlHistoryTableEntry {...entry} />}</For>
                            </tbody>
                        </table>
                    </div>
                )}
            </Query>
        </div>
    );
}
