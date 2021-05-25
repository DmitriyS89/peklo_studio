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
    <form  action="/login" method="post">
        <img class="mb-4" src="/images/logo.png" alt="" width="150" height="100">
        <h1 class="h3 mb-3 fw-normal">Авторизация</h1>

        <div class="form-floating">
            <input type="email" class="form-control" name="username" id="floatingInput" placeholder="name@example.com">
            <label for="floatingInput">Email</label>
        </div>
        <div class="form-floating">
            <input type="password" class="form-control" name="password" id="floatingPassword" placeholder="Пароль">
            <label for="floatingPassword">Пароль</label>
        </div>

        <#if _csrf??>
            <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
        </#if>

        <button class="w-100 btn btn-lg btn-primary mb-2" type="submit">Войти</button>
        <a href="/singup" class="forgot">У вас нет аккаунта?</a>
        <a href="/identify" class="forgot">Забыл пароль?</a>
        <p class="mt-5 mb-3 year">&copy;2021</p>
    </form>
</main>
</body>
</html>
