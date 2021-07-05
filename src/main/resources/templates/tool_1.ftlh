<#import "partials/main.ftlh" as main/>

<@main.renderWith>
    <h1 class="mt-4">Tool 1</h1>
    <div class="container-fluid">
        <p>*** Краулер для “Мониторинга изменений на сайте”
            Данный инструмент сканирует заданный сайт и оповещает по электронной почте о каких-либо изменениях в нём. Это позволяет, своевременно выявлять поехавшую вёрстку и останавливать рекламу до устранения неисправностей. Также полезен на случай внесения изменений со стороны клиента или разработчика в форму заявки, кнопки или других активных элементов на сайте.
        </p>
        <p>*** Вставьте (любую) ссылку в поле.</p>
        <p>*** Это нужно для некоторых сайтов где скрипты мешают 1-му инструменту.
        <ul>
            <li>Если в <b>Скрипты</b> поставили галку то скрипты внутри сайта удалятся. </li>
            <li>Если в <b>Скрипты</b> убрали галку то скрипты внутри сайта не удалятся. </li>
        </ul>
        </p>

        <a href="/tool_1/client-items">
            <button type="button" class="btn btn-info">Ваши сохранённые элементы</button>
        </a>

        <div class="w-100 text-danger my-4">
            <#if success == "true">
                <div class="bg-warning px-2">
                    Успешно!
                </div>
            <#elseif success = "false">
                <div class="bg-warning px-2">
                    Ошибка!
                </div>
            </#if>
        </div>

        <form class="mx-auto w-50 d-flex flex-row" action="/tool_1/run" method="get">
            <div class="my-auto mx-1">
                <input required type="url" class="form-control" name="url" id="exampleFormControlInput1"
                       placeholder="domain">
            </div>
            <div class="my-auto mx-1" style="display: flex;width: 20%;align-items: center;">
                <label for="ourScripts">Скрипты</label>
                <input id="ourScripts" type="checkbox" class="form-control" name="isScript">
            </div>
            <div class="my-auto mx-1">
                <button type="submit" class="btn btn-secondary">Go</button>
            </div>
            <#if _csrf??>
                <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
            </#if>
        </form>
    </div>
</@main.renderWith>