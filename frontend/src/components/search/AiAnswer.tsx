import { saveFetch } from '../../lib/safeFetch';
import { timed } from '../../lib/timed';
import Query from '../base/query/Query';
import { aiAnswerSchema } from '../../lib/schemas';
import { Show, useContext } from 'solid-js';
import { UserContext } from '../user/UserProvider';

export interface Props {
    query: string;
}

export default function (props: Props) {
    const user = useContext(UserContext);

    return (
        <Show when={user.user?.isPremiumUser && user.user?.configuration.shouldUseAi}>
            <div class="max-sm:flex max-sm:items-center max-sm:justify-center">
                <div class="center m-4 w-fit flex-col rounded-lg bg-neutral-500/80 p-4 shadow-2xl max-sm:w-[90%] sm:float-right sm:mr-10 sm:max-w-[calc(50%-var(--spacing)*40)] sm:min-w-60">
                    <h2 class="pb-2 sm:text-2xl">
                        <b> Ai answer</b>
                    </h2>
                    <Query
                        f={() => timed(() => saveFetch('/api/v1/ai/answer?q=' + encodeURIComponent(props.query), {}, aiAnswerSchema).catch(() => undefined), 'v1/ai/answer')}
                        inlineLoading={true}
                    >
                        {([result]) => (
                            <Show when={result} fallback={<p>Ai could not find an answer to your question. Are you a premium user?</p>}>
                                {result?.answer}
                            </Show>
                        )}
                    </Query>
                </div>
            </div>
        </Show>
    );
}
