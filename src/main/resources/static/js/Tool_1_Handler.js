'use strict';

let count = 0;

let elements = [];

let url = "http://localhost:8080";

let modalWindow = document.getElementById("tool_1_modal_window");

function openModal() {
    modalWindow.hidden = false;
    document.getElementById("tool_1_count").innerHTML = `${count}`;
}

function refreshAll() {
    window.location.reload(false);
}

function saveDatasets() {
    let value = JSON.stringify(elements).toString();
    document.getElementById("tool_1_elem-array-json").setAttribute("value", value);
    document.getElementById("tool_1_submit").submit();
}

function returnHome() {
    window.location.replace(url + "/tool_1/result"); //redirect
}

function closeModal() {
    modalWindow.hidden = true;
}

function getTarget(e) {
    let target = e.target;
    let state = target.getAttribute("highlightedTool1")
    if (state !== "true") {
        elements.push(target.outerHTML);
        target.classList.add("red-line-tool1");
        target.setAttribute("highlightedTool1", "true");
        count++;
    }
}

document.body.addEventListener('click', getTarget);

const tool1Links = `
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tool 1 html monitorning</title>
    <link rel="stylesheet" type="text/css" href="/css/for_tool_1.css">`;

//-----------------------------------------fetch

async function promise() {
    await fetch(url + '/tool_1/getSiteElements')
        .then(async response => {
            let json = JSON.parse((await response.text()).toString());
            let mini_div = document.getElementById("mini-body-for-site");
            mini_div.innerHTML = json.body;
            document.head.innerHTML = `${tool1Links} ${json.links}`;
        });
}

promise();