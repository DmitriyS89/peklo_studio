<#macro renderWith title="Peklo-Studio" dir="ltr" scripts=[] styles=[]>

    <!doctype html>
    <html lang="en">
    <head>

        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>${title}</title>

        <!-- Bootstrap core CSS -->
        <link href="/vendor/bootstrap/css/bootstrap.css" rel="stylesheet">

        <!-- Custom styles for this template -->
        <link href="/css/simple-sidebar.css" rel="stylesheet">
        <#list styles as styleSource>
            <link rel="stylesheet" href="${styleSource}">
        </#list>

    </head>
    <body>

    <div class="d-flex" id="wrapper">

        <!-- Sidebar -->
        <div class="bg-light border-right" id="sidebar-wrapper">
            <div class="sidebar-heading"><img style="width: 75px; height: 50px;" class="mx-auto d-block"
                                              src="/images/logo.png"></div>
            <div class="list-group list-group-flush">
                <a href="/tool_1" class="list-group-item list-group-item-action bg-light">Краулер (изменение на
                    сайте)</a>
                <a href="/tool_2" class="list-group-item list-group-item-action bg-light">Мониторинг и контроль ключевых
                    слов</a>
                <a href="/tool_3/result" class="list-group-item list-group-item-action bg-light">Инструмент,
                    определяющий открытие
                    ссылок
                    вне домена, в котором эта ссылка находится</a>
                <a href="/tool_4/result" class="list-group-item list-group-item-action bg-light">Инструмент,
                    определяющий дубликаты
                    страниц без 301 редиректов</a>
                <a href="/tool_5/result" class="list-group-item list-group-item-action bg-light">Парсер подписчиков со страниц
                    соцсетей
                    Facebook, Instagram и ВКонтакте</a>
            </div>
        </div>
        <!-- /#sidebar-wrapper -->

        <!-- Page Content -->
        <div id="page-content-wrapper">

            <nav class="navbar navbar-expand-lg navbar-light bg-light border-bottom">
                <button class="btn btn-primary" id="menu-toggle">Toggle Menu</button>

                <button class="navbar-toggler" type="button" data-toggle="collapse"
                        data-target="#navbarSupportedContent"
                        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav ml-auto mt-2 mt-lg-0">
                        <li class="nav-item mx-1">
                            <form action="/profile" method="get">
                                <button class="btn btn-primary" type="submit">Profile</button>
                            </form>
                        </li>
                        <li class="nav-item mx-1">
                            <form action="/logout" method="post">
                                <#if _csrf??>
                                    <input type="hidden" name="${(_csrf.parameterName)!'csrf'}"
                                           value="${(_csrf.token)!'--no-token--'}"/>
                                </#if>
                                <button class="btn btn-primary" type="submit">LogOut</button>
                            </form>
                        </li>
                    </ul>
                </div>
            </nav>

            <div class="container-fluid">
                <#nested>
            </div>
        </div>
        <!-- /#page-content-wrapper -->

    </div>
    <!-- /#wrapper -->

    <!-- Bootstrap core JavaScript -->
    <script src="/vendor/jquery/jquery.min.js"></script>
    <script src="/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    <!-- Menu Toggle Script -->
    <script>
        $("#menu-toggle").click(function (e) {
            e.preventDefault();
            $("#wrapper").toggleClass("toggled");
        });
    </script>

    <#list scripts as scriptSource>
        <script src="${scriptSource}"></script>
    </#list>

    </body>
    </html>
</#macro>