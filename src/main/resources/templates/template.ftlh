<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tool 1 html monitorning</title>
    <link rel="stylesheet" type="text/css" href="/css/for_tool_1.css">
<#--    css, ico, style links   -->
</head>
<body highlightedTool1="true">
<div class="tool_1_buttons" highlightedTool1="true">
    <button type="button" onclick="openModal()" highlightedTool1="true">Готово</button>
    <button type="button" onclick="refreshAll()" highlightedTool1="true">Сбросить</button>
    <button type="button" onclick="returnHome()" highlightedTool1="true">Отмена</button>
</div>
<div hidden id="tool_1_modal_window" highlightedTool1="true">
    <div class="modal_window" highlightedTool1="true">
        <div class="modal_header" highlightedTool1="true">
            <h3 highlightedTool1="true">Вы уверены своём выборе элементов?</h3>
        </div>
        <div class="modal_body" highlightedTool1="true">
            <p highlightedTool1="true">Сайт: ${client_url} </p>
            <p highlightedTool1="true">Было выделено <strong id="tool_1_count">0</strong> элементов</p>
            <div highlightedTool1="true" >Время проверки:
                <div highlightedTool1="true" class="times">
                    Каждые
                    <label highlightedTool1="true" class="time">
                        <input highlightedTool1="true" type="number" id="timeNumber" min="1" max="24" value="1">
                    </label>
                    <label highlightedTool1="true" class="time">
                        <select id="time_type" highlightedTool1="true">
                            <option value="d_" highlightedTool1="true">дня\дней</option>
                            <option value="h_" selected highlightedTool1="true">часа\часов</option>
                        </select>
                    </label>
                </div>
            </div>
        </div>
        <div class="modal_footer" highlightedTool1="true">
            <button onclick="saveDatasets()" highlightedTool1="true" disabled id="submitButtonTool1">Сохранить</button>
            <button onclick="closeModal()" highlightedTool1="true">Отмена</button>
        </div>
    </div>
</div>
<div id="mini-body-for-site" highlightedTool1="true">
<#--body-->
</div>
<form action="/tool_1/saveElements" method="post" id="tool_1_submit" hidden>
    <input type="hidden" name="elements" id="tool_1_elem-array-json">
    <input type="hidden" name="time" id="tool_1_time">
    <#if _csrf??>
        <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
    </#if>
</form>
<script src="/js/Tool_1_Handler.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
</body>
</html>