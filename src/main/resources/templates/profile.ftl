<#import "parts/common.ftl" as c>

<@c.page>
    <h3 class="mb-4">Edit profile</h3>
    <#if message??>
        <div class="alert alert-info" role="alert">${message}</div>
    </#if>
    <form method="post">
        <input type="hidden" name="_csrf" value="${ _csrf.token }"/>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label" for="email">Email</label>
            <div class="col-sm-6">
                <input type="email" name="email" class="form-control" id="email" value="${email!''}"/>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label" for="password">Password</label>
            <div class="col-sm-6">
                <input type="password" name="password" class="form-control" id="password"/>
            </div>
        </div>
        <div class="form-group row">
            <button type="submit" class="btn btn-primary">
                Save
            </button>
        </div>
    </form>
</@c.page>