import { saveFetch } from '../../lib/safeFetch';
import { timed } from '../../lib/timed';
import Query from '../base/query/Query';
import { aiSummarySchema } from '../../lib/schemas';
import { Show, useContext } from 'solid-js';
import { UserContext } from '../user/UserProvider';

export interface Props {
    link: string;
}

export default function (props: Props) {
    const user = useContext(UserContext);

    return (
        <Show when={user.user?.isPremiumUser && user.user?.configuration.shouldUseAi}>
            <Query
                f={() => timed(() => saveFetch('/api/v1/ai/summarize?l=' + encodeURIComponent(props.link), {}, aiSummarySchema).catch(() => undefined), 'v1/ai/summarize')}
                inlineLoading={true}
            >
                {([result]) => (
                    <Show when={result}>
                        <div class="center flex-col rounded-lg bg-neutral-600/80 p-2">
                            <h2 class="pb-2">
                                <b>Ai summary</b>
                            </h2>
                            {result?.summary}
                        </div>
                    </Show>
                )}
            </Query>
        </Show>
    );
}
