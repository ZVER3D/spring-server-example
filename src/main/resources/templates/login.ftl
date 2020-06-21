<#import "parts/common.ftl" as c>
<#import "parts/form.ftl" as f>

<@c.page>
<h3 class="mb-4">Login page</h3>
<@f.form "/login" false />
<a href="/registration">Register</a>
</@c.page>