<#import "parts/common.ftl" as c>
<#import "parts/form.ftl" as f>

<@c.page>
    <h3 class="mb-4">Registration page</h3>
    <#if message??>
        <div class="alert alert-danger" role="alert">${message}</div>
    </#if>
    <@f.form "/registration" true />
    <a href="/login">Login</a>
</@c.page>