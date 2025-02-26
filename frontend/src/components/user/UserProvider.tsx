import { createContext, type JSX } from 'solid-js';
import { userSchema, type User } from '../../lib/schemas';
import Query from '../base/query/Query';
import { saveFetch } from '../../lib/safeFetch';
import QueryController from '../base/query/QueryController';
import { timed } from '../../lib/timed';

export const UserContext = createContext<{ user: User | undefined }>({ user: undefined });

function Provider(props: { children: JSX.Element; user: User | undefined }) {
    return <UserContext.Provider value={{ user: props.user }}>{props.children}</UserContext.Provider>;
}

function Wrapped(props: { children: JSX.Element; inlineLoading: boolean }) {
    return (
        <Query f={() => timed(() => saveFetch('/api/v1/info', {}, userSchema).catch(() => undefined), 'v1/info')} inlineLoading={props.inlineLoading} queryKey="user">
            {([user]) => <Provider user={user} children={props.children} />}
        </Query>
    );
}

export default function (props: { children: JSX.Element; inlineLoading?: boolean }) {
    return (
        <QueryController>
            <Wrapped inlineLoading={props.inlineLoading ?? false}>{props.children}</Wrapped>
        </QueryController>
    );
}
