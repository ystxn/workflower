import React, { useEffect, useState } from 'react';
import ReactDOM from 'react-dom/client';
import '@symphony-ui/uitoolkit-styles/dist/css/uitoolkit.css';
import {
    Button,
    Dropdown,
} from "@symphony-ui/uitoolkit-components/components";
import styled from "styled-components";
import { editor } from 'monaco-editor';
import Editor from './editor';
import Api from './api';

const FlexBar = styled.div`
    display: flex;
    gap: 1rem;
`;

const Root = styled(FlexBar)`
    font-family: SymphonyLato, serif;
    font-size: 1rem;
    flex-direction: column;
`;

const TopBar = styled.div`
    display: grid;
    grid-template-columns: 4fr 1fr;
    align-items: flex-end;
    gap: .5rem;
`;

const BottomFlexBar = styled(FlexBar)`
    align-items: center;
    font-weight: bold;
    color: green;
`;

const MyDropdown = () => (
    <Dropdown
        label="Select Workflow"
        menuPortalStyles={{ width: '100%' }}
        enableTermSearch
        onChange={() => {}}
        onTermSearch={function noRefCheck() {}}
        options={[
            { label: 'Option 1', value: '1' },
            { label: 'Option 2', value: '2' },
            { label: 'Option 3', value: '3' },
            { label: 'Option 4', value: '4' },
            { label: 'Option 5', value: '5' },
        ]}
    />
);

const BottomBar = ({ contents }) => {
    const [ status, setStatus ] = useState('');

    const saveWorkflow = (contents) => {
        Api('write-workflow', { contents }, () => {
            setStatus('Saved!');
            setTimeout(() => {
                setStatus('');
            }, 2000);
        });
    }

    return (
        <BottomFlexBar>
            <Button onClick={() => saveWorkflow(contents)}>
                Save Workflow
            </Button>
            <div>{status}</div>
        </BottomFlexBar>
    );
};

const App = () => {
    const [ contents, setContents ] = useState();

    useEffect(() => {
        Api('read-workflow', null, (res) => setContents(res.contents));
    }, []);

    return (
        <Root>
            <TopBar>
                <MyDropdown />
                <Button>Delete Workflow</Button>
            </TopBar>

            <Editor {...{ editor, contents }} />

            <BottomBar {...{ contents }} />
        </Root>
    );
}

ReactDOM.createRoot(document.querySelector('#root')).render(<App />);
