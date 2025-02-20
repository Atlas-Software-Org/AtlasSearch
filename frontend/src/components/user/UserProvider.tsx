import { createContext, type JSX } from 'solid-js';
import { userSchema, type User } from '../../schemas';
import Query from '../base/query/Query';
import { fetchAndValidate } from '../../validatedFetch';
import Loading from '../base/loading/Loading';
import QueryController from '../base/query/QueryController';
import { timed } from '../../timed';

export const UserContext = createContext<{ user: User | undefined }>({ user: undefined });

function Provider(props: { children: JSX.Element; user: User | undefined }) {
    return <UserContext.Provider value={{ user: props.user }}>{props.children}</UserContext.Provider>;
}

function Wrapped(props: { children: JSX.Element; inlineLoading: boolean }) {
    return (
        <Query
            f={() => timed(() => fetchAndValidate(userSchema, () => fetch('/api/v1/info')).catch(() => undefined), 'userInfo')}
            inlineLoading={props.inlineLoading}
            queryKey="user"
        >
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
