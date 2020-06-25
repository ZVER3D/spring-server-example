<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" class="form-inline">
                <input
                        type="text"
                        name="filter"
                        placeholder="Search by tag"
                        class="form-control mr-2"
                        value="${filter!}"/>
                <button type="submit" class="btn btn-primary">Find</button>
            </form>
        </div>
    </div>
    <div>
        <h3>Messages</h3>
        <#include "parts/messageList.ftl">
    </div>
    <#if user??>
        <hr/>
        <div class="mb-5 pb-5">
            <#include "parts/messageEdit.ftl">
        </div>
    </#if>
</@c.page>
