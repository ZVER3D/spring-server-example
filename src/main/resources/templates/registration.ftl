<#import "parts/common.ftl" as c>
<#import "parts/form.ftl" as f>

<@c.page>
<h1>Registration page</h1>
<b>${message}</b>
<@f.form "/registration" />
<a href="/login">Login</a>
</@c.page>