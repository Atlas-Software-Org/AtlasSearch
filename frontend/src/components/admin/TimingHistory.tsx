import { For } from 'solid-js';
import { saveFetch } from '../../lib/safeFetch';
import { timed } from '../../lib/timed';
import Query from '../base/query/Query';
import { SolidApexCharts } from 'solid-apexcharts';
import { timingsAveragesSchema, type TimingAverageEntry } from '../../lib/schemas';

function TimingChart(props: { data: TimingAverageEntry[]; key: string }) {
    return (
        <div class="center p-2">
            <div class="field h-96 w-3/4 max-sm:w-full">
                <div class="center">
                    <h2 class="text-xl">{props.key}</h2>
                </div>
                <SolidApexCharts
                    width="100%"
                    height="80%"
                    type="bar"
                    options={{
                        xaxis: {
                            categories: props.data.map((d) => d.date),
                        },
                        theme: {
                            mode: 'dark',
                        },
                    }}
                    series={[
                        {
                            data: props.data.map((d) => d.timing),
                        },
                    ]}
                />
            </div>
        </div>
    );
}

export default function () {
    return (
        <Query f={() => timed(() => saveFetch('/api/v1/admin/timingAverage', {}, timingsAveragesSchema), 'timingAverage')}>
            {([history]) => <For each={Object.keys(history)}>{(key) => <TimingChart data={history[key].toReversed()} key={key} />}</For>}
        </Query>
    );
}
