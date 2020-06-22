<#import "parts/common.ftl" as c>

<@c.page>
    <h3>Users</h3>
    <table>
        <thead>
        <tr>
            <th>name</th>
            <th>roles</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td>${user.username}</td>
                <td>
                    <#list user.roles as role>${role}<#sep>, </#list>
                </td>
                <td>
                    <a href="/user/${user.id}">Edit</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>