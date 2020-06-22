<#import "parts/common.ftl" as c>
<#import "parts/form.ftl" as f>

<@c.page>
    <h3 class="mb-4">Login page</h3>
    <#if message??>
        <div class="alert alert-info" role="alert">${message}</div>
    </#if>
    <@f.form "/login" false />
    <a href="/registration">Register</a>
</@c.page>