import SvgButton from './SvgButton';

export interface Props {
    callback: () => void;
}

import editSvg from '../assets/edit.svg?url';

export default function (props: Props) {
    return <SvgButton src={editSvg} callback={props.callback} />;
}
