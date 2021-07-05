<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
    <meta name="generator" content="Hugo 0.82.0">
    <title>Log in Peklo-Studio</title>

    <!-- Bootstrap core CSS -->
    <link href="/vendor/bootstrap/css/bootstrap.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/css/signin.css" rel="stylesheet">

</head>
<body class="text-center">
<main class="form-signin">
    <form action="/identify" method="post">
        <img class="mb-4" src="/images/PEKLO-LOGO-BLACK-500x500.png" alt="" width="150" height="150">
        <h1 class="h3 mb-3 fw-normal">Восстановить пароль</h1>
        <div class="form-floating">
            <#if accept == "error">
                <input type="email" class="form-control" name="userEmail" id="floatingInput" placeholder="Адрес не найден!">
            <#else>
                <input type="email" class="form-control" name="userEmail" id="floatingInput">
            </#if>
            <label for="floatingInput">Введите вашу электронную почту</label>
        </div>
        <button class="w-100 btn btn-lg btn-primary mb-2" type="submit">Далее</button>
        <#if _csrf??>
            <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
        </#if>
    </form>
</main>
</body>
</html>