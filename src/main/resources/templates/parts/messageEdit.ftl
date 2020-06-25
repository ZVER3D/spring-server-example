<a class="btn btn-primary mb-2"
   data-toggle="collapse"
   href="#collapse"
   role="button"
   aria-expanded="false"
   aria-controls="collapse">
    Message Editor
</a>
<div class="collapse <#if textError??>show</#if>" id="collapse">
    <div class="form-group">
        <form method="post" enctype="multipart/form-data">
            <input type="hidden" name="_csrf" value="${ _csrf.token }"/>
            <input type="hidden" name="id" value="<#if message??>${message.id}</#if>"/>
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
                <button type="submit" class="btn btn-primary">Save message</button>
            </div>
        </form>
    </div>
</div>