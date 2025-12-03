<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>${category.isEdit ? 'Chỉnh sửa Category' : 'Thêm mới Category'}</title>
    
    <jsp:include page="/WEB-INF/views/common/head.jsp" />
</head>

<body class="bg-light">

    <jsp:include page="/WEB-INF/views/admin/fragments/header.jsp" />

    <div class="container mt-5 mb-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow-sm">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0">
                            <i class="fa-solid fa-pen-to-square"></i> 
                            <c:choose>
                                <c:when test="${category.isEdit}">Chỉnh sửa Category</c:when>
                                <c:otherwise>Thêm mới Category</c:otherwise>
                            </c:choose>
                        </h4>
                    </div>
                    
                    <div class="card-body">
                        <form action="<c:url value='/admin/categories/saveOrUpdate' />" method="POST">
                            
                            <input type="hidden" name="isEdit" value="${category.isEdit}" />

                            <div class="mb-3">
                                <label for="categoryId" class="form-label fw-bold">Category ID:</label>
                                <input type="text" class="form-control" id="categoryId" name="categoryId" 
                                       value="${category.categoryId}" readonly 
                                       placeholder="ID tự động sinh ra" style="background-color: #e9ecef;">
                            </div>

                            <div class="mb-3">
                                <label for="categoryName" class="form-label fw-bold">Tên Danh Mục:</label>
                                <input type="text" class="form-control" id="categoryName" name="categoryName" 
                                       value="${category.categoryName}" required placeholder="Ví dụ: Laptop, Mobile...">
                            </div>

                            <div class="mb-3">
                                <label for="categoryCode" class="form-label fw-bold">Mã Danh Mục (Code):</label>
                                <input type="text" class="form-control" id="categoryCode" name="categoryCode" 
                                       value="${category.categoryCode}" required placeholder="Ví dụ: LT01, MB02...">
                            </div>

                            <div class="mb-3">
                                <label for="images" class="form-label fw-bold">Link Hình Ảnh:</label>
                                <input type="text" class="form-control" id="images" name="images" 
                                       value="${category.images}" placeholder="Dán URL ảnh vào đây">
                                <c:if test="${not empty category.images}">
                                    <div class="mt-2">
                                        <img src="${category.images}" alt="Preview" class="img-thumbnail" style="height: 100px;">
                                    </div>
                                </c:if>
                            </div>

                            <div class="mb-3">
                                <label for="status" class="form-label fw-bold">Trạng Thái:</label>
                                <select class="form-select" id="status" name="status">
                                    <option value="1" ${category.status == 1 ? 'selected' : ''}>Hoạt động (Active)</option>
                                    <option value="0" ${category.status == 0 ? 'selected' : ''}>Khóa (Inactive)</option>
                                </select>
                            </div>

                            <div class="d-flex justify-content-end gap-2 mt-4">
                                <a href="<c:url value='/admin/categories' />" class="btn btn-secondary">
                                    <i class="fa-solid fa-arrow-left"></i> Quay lại
                                </a>
                                <button type="submit" class="btn btn-success">
                                    <i class="fa-solid fa-floppy-disk"></i> Lưu lại
                                </button>
                            </div>

                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>