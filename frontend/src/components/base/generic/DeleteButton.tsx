import { createSignal, useContext } from 'solid-js';
import ContinueBox from './ContinueBox';
import SvgButton from './SvgButton';
import { LoadingContext, type LoadingInterface } from '../loading/Loading';

import deleteSvg from '../assets/delete.svg?url';

export interface Props<T> {
    callback: (id: T, loading: LoadingInterface) => void;
    id: T;
}

export default function <T>(props: Props<T>) {
    const loading = useContext(LoadingContext);
    const [continueVisible, setContinueVisible] = createSignal(false);
    return (
        <>
            <SvgButton src={deleteSvg} callback={() => setContinueVisible(true)} />
            <ContinueBox
                visible={continueVisible()}
                resetCallback={() => setContinueVisible(false)}
                cancelCallback={() => {}}
                continueCallback={() => props.callback(props.id, loading)}
            >
                Continue deleting the element?
            </ContinueBox>
        </>
    );
}
