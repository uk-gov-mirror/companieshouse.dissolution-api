package uk.gov.companieshouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.companieshouse.model.dto.DissolutionCreateRequest;
import uk.gov.companieshouse.model.dto.DissolutionCreateResponse;
import uk.gov.companieshouse.service.DissolutionService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/dissolution-request/{company-number}")
public class DissolutionController extends BaseController {

    private final DissolutionService dissolutionService;

    public DissolutionController(DissolutionService dissolutionService) {
        super();
        this.dissolutionService = dissolutionService;
    }

    @Operation(summary = "Create Dissolution Request", tags = "Dissolution")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Dissolution Request created"),
        @ApiResponse(responseCode = "400", description = "Missing headers"),
        @ApiResponse(responseCode = "401", description = "Unauthorised"),
        @ApiResponse(responseCode = "409", description = "Dissolution Request already exists for company"),
        @ApiResponse(responseCode = "422", description = "Invalid request format"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DissolutionCreateResponse> submitDissolutionRequest(
        @RequestHeader("ERIC-identity") String userId,
        @RequestHeader("ERIC-Authorised-User") String email, // TODO - extract email from header
        @PathVariable("company-number") final String companyNumber,
        @Valid @RequestBody final DissolutionCreateRequest body,
        HttpServletRequest request) {

        if (StringUtils.isBlank(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (dissolutionService.doesDissolutionRequestExistForCompany(companyNumber)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        try {
            final DissolutionCreateResponse response = dissolutionService.create(body, companyNumber, userId, request.getRemoteAddr(), email);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
