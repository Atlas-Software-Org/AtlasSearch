import { createContext, type JSX, useContext } from 'solid-js';

import menuSvg from '../assets/menu.svg?url';

const NavigationContext = createContext({ reset: () => {} });

export function Home(props: { children: JSX.Element; href: string }) {
    const navigation = useContext(NavigationContext);

    return (
        <a onClick={navigation.reset} class="m-4 hover:underline hover:decoration-black" href={props.href}>
            {props.children}
        </a>
    );
}

export function Entry(props: { children: JSX.Element; href: string }) {
    const navigation = useContext(NavigationContext);

    return (
        <a onClick={navigation.reset} class="entry m-4 hidden h-fit hover:underline hover:decoration-black sm:block" href={props.href}>
            {props.children}
        </a>
    );
}

export default function (props: { children: JSX.Element }) {
    let navigation: HTMLDivElement | undefined;

    const reset = () => {
        navigation?.classList.remove('responsive');
    };

    return (
        <>
            <div ref={navigation} class="fixed flex h-fit w-full flex-row justify-between bg-neutral-800 shadow-md">
                <div class="flex flex-col sm:flex-row">
                    <NavigationContext.Provider value={{ reset }}>{props.children}</NavigationContext.Provider>
                </div>

                <a
                    onClick={(e) => {
                        e.preventDefault();
                        if (navigation?.classList.contains('responsive')) {
                            reset();
                        } else {
                            navigation?.classList.add('responsive');
                        }
                    }}
                    class="relative -top-1 m-4 h-0 overflow-visible hover:underline hover:decoration-black sm:hidden"
                >
                    <img src={menuSvg} alt="Menu" class="w-8" />
                </a>
            </div>

            <div class="pt-16" />
        </>
    );
}
