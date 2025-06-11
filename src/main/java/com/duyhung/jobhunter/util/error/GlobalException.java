package com.duyhung.jobhunter.util.error;


import com.duyhung.jobhunter.domain.response.RestResponse;
import jakarta.persistence.NoResultException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice
//public class GlobalException {
//    @ExceptionHandler(value = {
//            UsernameNotFoundException.class,
//            BadCredentialsException.class
//    })
//    public ResponseEntity<RestResponse<Object>> handleIdException(Exception ex) {
//        RestResponse<Object> res  = new RestResponse<Object>();
//        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
//        res.setError(ex.getMessage());
//        res.setMessage("Exception occured");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res)  ;
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<RestResponse<Object>> validException(MethodArgumentNotValidException ex) {
//        RestResponse<Object> res = new RestResponse<>();
//        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
//        res.setMessage("Validation failed");
//
//        // Lấy thông báo lỗi đầu tiên
//        String errorMessage = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .findFirst()
//                .map(error -> error.getField() + ": " + error.getDefaultMessage())
//                .orElse("Invalid input");
//
//        res.setError(errorMessage);
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
//    }
//}

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            IdInvalidException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleIdException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage(ex.getMessage());
        res.setError("Exception occurred");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    // Xử lý lỗi validation
    @ExceptionHandler(value = {
            NoResultException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleNoResultException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setMessage(ex.getMessage());
        res.setError("404 Not Found, URL may not exist");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)

    public ResponseEntity<RestResponse<Object>> validException(MethodArgumentNotValidException ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Validation failed");

        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        });

        res.setMessage(errorMessage.toString().isEmpty() ? "Invalid input" : errorMessage.toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = {

            StorageException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleFileUploadException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage(ex.getMessage());
        res.setError("Exception uploading file");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = {
            PermissionException.class,
    })
    public ResponseEntity<Object> handlePermissionException(Exception ex){
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.FORBIDDEN.value());
        res.setError("FORBIDDEN");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(res);
    }

}
