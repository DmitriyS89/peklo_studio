<#import "partials/main.ftlh" as main/>

<@main.renderWith>
    <h1 class="mt-4">Парсер подписчиков со страниц
        соцсетей
        Facebook, Instagram и ВКонтакте</h1>
    <div class="container-fluid">
        <p>*** There will be information of instrument</p>
        <p>*** There will be information of instrument</p>
        <p>*** There will be information of instrument</p>


        <form class="mx-auto w-50 d-flex flex-row" action="/tool_5/run" method="post">
            <div class="form-group">
                <input required type="text" class="form-control" name="urlFromFront" id="exampleFormControlInput1"
                       placeholder="domain">
            </div>
            <div class="form-group">
                <select class="form-control" name="social" id="exampleFormControlSelect1">
                    <option>vk</option>
                    <option>facebook</option>
                    <option>instagram</option>
                </select>
            </div>
            <div class="form-group">
                <select class="form-control" name="kind" id="exampleFormControlSelect1">
                    <option>user</option>
                    <option>group</option>
                </select>
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-secondary">Go</button>
            </div>
            <#if _csrf??>
                <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
            </#if>
        </form>

        <div class="w-100 text-danger my-4">
            <#if success == "true">
                <div class="bg-warning px-2">
                    Успешно, результат отправлен на электронную почту.
                </div>
            <#elseif success = "false">
                <div class="bg-warning px-2">
                    ошибка
                </div>
            </#if>
        </div>
    </div>
</@main.renderWith>