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
        <ul style="padding: 0;" class="card-columns">
            <#list messages as message>
                <li class="card my-3">
                    <#if message.filename??>
                        <img src="/img/${message.filename}" class="card-img-top" alt="">
                    </#if>
                    <div class="m-2">
                        <span>${message.text}</span>
                        <i>${message.tag}</i>
                    </div>
                    <div class="card-footer text-muted">
                        ${message.authorName}
                    </div>
                </li>
            <#else>
                No messages
            </#list>
        </ul>
    </div>
    <#if user??>
        <hr/>
        <div class="mb-5 pb-5">
            <a class="btn btn-primary mb-2"
               data-toggle="collapse"
               href="#collapse"
               role="button"
               aria-expanded="false"
               aria-controls="collapse">
                Add message
            </a>
            <div class="collapse <#if textError??>show</#if>" id="collapse">
                <div class="form-group">
                    <form method="post" enctype="multipart/form-data">
                        <input type="hidden" name="_csrf" value="${ _csrf.token }"/>
                        <div class="form-group">
                            <input
                                    type="text"
                                    name="text"
                                    class="form-control mb-2 ${(textError??)?string('is-invalid', '')}"
                                    value="<#if message??>${message.text}</#if>"
                                    placeholder="Your message"/>
                            <#if textError??>
                                <div class="invalid-feedback">
                                    ${textError}
                                </div>
                            </#if>
                        </div>
                        <div class="form-group">
                            <input
                                    type="text"
                                    name="tag"
                                    class="form-control mb-2 ${(tagError??)?string('is-invalid', '')}"
                                    value="<#if message??>${message.text}</#if>"
                                    placeholder="Tag"/>
                            <#if tagError??>
                                <div class="invalid-feedback">
                                    ${tagError}
                                </div>
                            </#if>
                        </div>
                        <div class="form-group">
                            <div class="custom-file mb-2">
                                <input type="file" name="file" id="file"/>
                                <label class="custom-file-label" for="file">Choose file</label>
                            </div>
                        </div>
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">Send</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </#if>
</@c.page>
