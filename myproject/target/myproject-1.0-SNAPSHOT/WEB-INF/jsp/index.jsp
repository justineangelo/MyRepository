<%-- 
    Document   : index
    Created on : Feb 24, 2015, 9:17:13 AM
    Author     : noc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <style>
            .glyphicon-refresh-animate {
                -animation: spin .7s infinite linear;
                -webkit-animation: spin2 .7s infinite linear;
            }

            @-webkit-keyframes spin2 {
                from { -webkit-transform: rotate(0deg);}
                to { -webkit-transform: rotate(360deg);}
            }

            @keyframes spin {
                from { transform: scale(1) rotate(0deg);}
                to { transform: scale(1) rotate(360deg);}
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Store ${msg}</title>
        <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
        <script type="application/javascript"> 
            $(document).ready(function() {
                $('#buy').click(function ()
                { 
                    $(this).html('<span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span> Loading...');
                    $(this).attr('disabled','disabled');
                    $("#alert-notifier").hide();
                    $.ajax({
                    url: "/myproject/service/eStore/api?username=" + $('#username').val() + "&&itemname=" + $('#itemname').val(),
                    success: function(data){
                        if(data.statusCode == 100)
                        {
                            $("#alert-notifier").removeClass( "alert alert-warning" ).addClass( "alert alert-success" );
                            $("#alert-notifier").html("<strong>" + "Transaction Successful" + "</strong>").show(0).delay(4000).hide(0);
                        }
                        else
                        {
                            $("#alert-notifier").removeClass( "alert alert-success" ).addClass( "alert alert-warning" );
                            $("#alert-notifier").html("<strong>" + data.statusDesc + "</strong>").show(0).delay(4000).hide(0);
                        }
                        $("#buy").html('BUY');
                        $("#buy").removeAttr('disabled');
                    },
                    dataType: "json"
                    });
                    
                });

            });
        </script>
    </head>
    <body>
        <div class="container theme-showcase">
            <div><h1>Online Shopping</h1></div>
            <div class="row">
                <div class="col-md-6">
                    <table class="table">
                        <tbody>
                            <tr>
                                <td>Username :</td>
                                <td><input id="username" name="username" value="" /></td>
                            </tr>
                            <tr>
                                <td>ItemName: :</td>
                                <td><input id="itemname" name="itemname" value="" /></td>
                            </tr>
                        </tbody>
                    </table>
                    <table class="table">
                        <tbody>
                            <tr>
                                <td>
                                    <button class="btn btn-lg btn-primary" id="buy" >BUY</button>
                                </td>
                                <td>
                                    <div id="alert-notifier" class="" role="alert">
                                </td>
                            </tr>
                        </tbody>
                    </table>
                  
                </div>
            </div>
        </div>
    </body>
</html>
