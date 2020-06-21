<#import "parts/common.ftl" as c>
<#import "parts/form.ftl" as f>

<@c.page>
<h3 class="mb-4">Registration page</h3>
<b>${message?ifExists}</b>
<@f.form "/registration" true />
<a href="/login">Login</a>
</@c.page>