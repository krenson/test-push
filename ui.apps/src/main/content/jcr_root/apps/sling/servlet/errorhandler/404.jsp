<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>Page indisponible</title>
    <link href="/etc.clientlibs/leforemhe/clientlibs/clientlib-dependencies.css" rel="stylesheet">
    <style>
        body {
            font-family: "robotoregular";
            font-size: 18px;
        }

        .container {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin: 30px 0;
            padding: 0 10%;
        }

        .circle {
            -webkit-box-align: center;
            -ms-flex-align: center;
            align-items: center;
            border: 15px solid #B8B8B8;
            border-radius: 50%;
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex;
            -webkit-box-orient: vertical;
            -webkit-box-direction: normal;
            -ms-flex-direction: column;
            flex-direction: column;
            height: 160px;
            padding: 50px 40px 30px 40px;
            width: 160px;
        }

        .eyes {
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex;
            -webkit-box-orient: horizontal;
            -webkit-box-direction: normal;
            -ms-flex-direction: row;
            flex-direction: row;
            -webkit-box-pack: justify;
            -ms-flex-pack: justify;
            justify-content: space-between;
            margin-bottom: 50px;
        }

        .eye {
            border: 15px solid #B8B8B8;
            border-radius: 50%;
            height: 30px;
            margin: 0 20px 0 20px;
            width: 30px;
        }

        .mouth {
            border: 7px solid #B8B8B8;
            border-radius: 7px;
            height: 0;
            -webkit-transform: rotate(-22deg);
            -ms-transform: rotate(-22deg);
            transform: rotate(-22deg);
            width: 95px;
        }

        .Page_indisponible {
            font-size: 2.667rem;
            color: rgb(147, 170, 79);
            font-weight: bold;
        }

        p {
            font-size: 1rem;
            color: rgb(44, 62, 80);
        }

        a {
            color: rgb(136, 157, 73);
            text-decoration: underline;
        }

        a:hover {text-decoration: none;}

        .bouton {
            background-color: #93aa4f;
            border-radius: 4px;
            display: inline-flex;
            align-items: center;
            font-size: 1.21rem;
            color: rgb(255, 255, 255);
            font-weight: bold;
            margin: 35px 0;
            padding: 15px 20px;
            text-decoration: none;
            text-transform: uppercase;
        }

        .bouton:hover {
            -webkit-box-shadow: 0 2px 3px #333;
            box-shadow: 0 2px 3px #333;
        }

        .flecheBlanche {
            border: 4px solid #FFF;
            border-bottom: 0;
            border-left: 0;
            height: 10px;
            margin-left: 20px;
            transform: rotate(45deg);
            width: 10px;
        }

        .socialNetwork {
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex;
            -webkit-box-pack: start;
            -ms-flex-pack: start;
            justify-content: flex-start;
            margin: 22px 0;
            padding: 0;
        }

        .socialNetwork li {
            list-style-type: none;
            margin-right: 30px;
        }

    </style>
</head>

<body>
<div class="container">
    <div>
        <div class="circle">
            <div class="eyes">
                <div class="eye"></div>
                <div class="eye"></div>
            </div>
            <div class="mouth"></div>
        </div>
    </div>
    <div>
        <p class="Page_indisponible">Page indisponible</p>
        <p>Elle a peut-être été supprimée ou déplacée. Vous pouvez essayer de la retrouver dans le <a href="/plan.html">plan du site.</a></p>
        <a class="bouton" href="/">accueil du site<div class="flecheBlanche"></div></a>
        <div>
            Retrouvez-nous sur les réseaux sociaux&nbsp;!<br>
            <ul class="socialNetwork">
                <li><a href="https://www.facebook.com/leforemhe/" target="_blank"><img src="/etc.clientlibs/leforemhe/clientlibs/clientlib-site/resources/images/socialmedia/facebook-logo.png" /></a></li>
                <li><a href="https://twitter.com/leforemhe" target="_blank"><img src="/etc.clientlibs/leforemhe/clientlibs/clientlib-site/resources/images/socialmedia/twitter-social-logotype.png" /></a></li>
                <li><a href="https://www.youtube.com/user/Videosleforemhe/" target="_blank"><img src="/etc.clientlibs/leforemhe/clientlibs/clientlib-site/resources/images/socialmedia/youtube-logotype.png" /></a></li>
                <li><a href="https://www.linkedin.com/company/le-forem" target="_blank"><img src="/etc.clientlibs/leforemhe/clientlibs/clientlib-site/resources/images/socialmedia/linkedin-logo.png" /></a></li>
            </ul>

        </div>

    </div>
</div>
</body>
</html>