'use strict';

let count = 0;

let elements = [];

let url = "http://localhost:8080";

let modalWindow = document.getElementById("tool_1_modal_window");

function openModal() {
    openButton();
    modalWindow.hidden = false;
    document.getElementById("tool_1_count").innerHTML = `${count}`;
}

function refreshAll() {
    window.location.reload(false);
}

function preparationTime() {
    let timeNumber = document.getElementById("timeNumber").value;
    let timeType = document.getElementById("time_type").value;
    if(timeNumber < 1) timeNumber = 1;
    return timeType + Math.round(timeNumber);
}

function saveDatasets() {
    let value = JSON.stringify(elements).toString();
    let time = preparationTime();
    document.getElementById("tool_1_time").setAttribute("value", time)
    document.getElementById("tool_1_elem-array-json").setAttribute("value", value);
    document.getElementById("tool_1_submit").submit();
}

function returnHome() {
    window.location.replace(url + "/tool_1/result"); //redirect
}

function closeModal() {
    modalWindow.hidden = true;
}

function openButton() {
    if(elements.length > 0) {
        let elementById = document.getElementById("submitButtonTool1");
        elementById.removeAttribute("disabled")
    }
}

function getTarget(e) {
    let target = e.target;
    let state = target.getAttribute("highlightedTool1")
    if (state !== "true") {
        let cssPath = dompath(target).toCSS();
        elements.push(cssPath);
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

//Simple experimental library which generates CSS selectors from DOM nodes.
//https://github.com/jhartikainen/dompath

(function(window, document) {
    window.dompath = function(el, parent) {
        parent = parent || document.body;
        if(el.nodeName) {
            return new DomPath(pathNode(el, parent));
        }

        return new DomPath(el.node);
    };

    let getSelector = function(node) {
        if(node.id !== '') {
            return '#' + node.id;
        }

        let root = '';
        if(node.parent) {
            root = getSelector(node.parent) + ' > ';
        }

        return root + node.name + ':nth-child(' + (node.index + 1) + ')';
    };

    let DomPath = function(node) { this.node = node; };
    DomPath.prototype = {
        toCSS: function() {
            return getSelector(this.node);
        },

        select: function() {
            if(this.node.id !== '') {
                return document.getElementById(this.node.id);
            }

            return document.querySelector(this.toCSS());
        }
    };

    let pathNode = function(el, root) {
        let node = {
            id: el.id,
            name: el.nodeName.toLowerCase(),
            index: childIndex(el),
            parent: null
        };

        if(el.parentElement && el.parentElement !== root) {
            node.parent = pathNode(el.parentElement, root);
        }

        return node;
    };

    let childIndex = function(el) {
        let idx = 0;
        while(el = el.previousSibling) {
            if(el.nodeType == 1) {
                idx++;
            }
        }

        return idx;
    };
})(window, document);