package com.makersharks.services;

import com.makersharks.dto.SupplierCreateDTO;
import com.makersharks.entities.*;
import com.makersharks.exception.exception.ResourceNotFoundException;
import com.makersharks.repositories.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testCreateSupplier() {
        SupplierCreateDTO dto = new SupplierCreateDTO();
        dto.setCompanyName("ABC Corp");
        dto.setWebsite("http://abc.com");
        dto.setLocation("India");
        dto.setNatureOfBusiness("small_scale");
        dto.setManufacturingProcesses("three_d_printing");

        Supplier supplier = new Supplier();
        supplier.setCompanyName(dto.getCompanyName());
        supplier.setWebsite(dto.getWebsite());
        supplier.setLocation(dto.getLocation());
        supplier.setNatureOfBusiness(NatureOfBusiness.SMALL_SCALE);
        supplier.setManufacturingProcesses(ManufacturingProcess.THREE_D_PRINTING);


        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        Supplier createdSupplier = supplierService.createSupplier(dto);

        assertNotNull(createdSupplier);
        assertEquals("ABC Corp", createdSupplier.getCompanyName());
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }

    @Test
    void testGetSuppliersByCriteria() {
        String location = "India";
        String natureOfBusiness = "small_scale";
        String manufacturingProcess = "three_d_printing";
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "companyName";
        String sortDir = "asc";

        Supplier supplier1 = new Supplier();
        supplier1.setCompanyName("ABC Corp");
        supplier1.setLocation(location);
        supplier1.setNatureOfBusiness(NatureOfBusiness.SMALL_SCALE);
        supplier1.setManufacturingProcesses(ManufacturingProcess.THREE_D_PRINTING);

        Supplier supplier2 = new Supplier();
        supplier2.setCompanyName("XYZ Ltd");
        supplier2.setLocation(location);
        supplier2.setNatureOfBusiness(NatureOfBusiness.SMALL_SCALE);
        supplier2.setManufacturingProcesses(ManufacturingProcess.THREE_D_PRINTING);

        List<Supplier> supplierList = Arrays.asList(supplier1, supplier2);

        Page<Supplier> supplierPage = new PageImpl<>(supplierList);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, sortBy));

        when(supplierRepository.findByLocationAndNatureOfBusinessAndManufacturingProcesses(
                eq(location), eq(NatureOfBusiness.SMALL_SCALE), eq(ManufacturingProcess.THREE_D_PRINTING), eq(pageable)
        )).thenReturn(supplierPage);

        List<Supplier> foundSuppliers = supplierService.getSuppliersByCriteria(
                location, natureOfBusiness, manufacturingProcess, pageNo, pageSize, sortBy, sortDir
        );

        assertEquals(2, foundSuppliers.size());
        assertEquals("ABC Corp", foundSuppliers.get(0).getCompanyName());
        verify(supplierRepository, times(1)).findByLocationAndNatureOfBusinessAndManufacturingProcesses(
                eq(location), eq(NatureOfBusiness.SMALL_SCALE), eq(ManufacturingProcess.THREE_D_PRINTING), eq(pageable)
        );
    }

    @Test
    void testGetSuppliersByCriteria_ResourceNotFound() {
        String location = "Unknown";
        String natureOfBusiness = "small_scale";
        String manufacturingProcess = "three_d_printing";
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "companyName";
        String sortDir = "asc";

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, sortBy));

        Page<Supplier> emptyPage = Page.empty();

        when(supplierRepository.findByLocationAndNatureOfBusinessAndManufacturingProcesses(
                eq(location), eq(NatureOfBusiness.SMALL_SCALE), eq(ManufacturingProcess.THREE_D_PRINTING), eq(pageable)
        )).thenReturn(emptyPage);

        assertThrows(ResourceNotFoundException.class, () -> supplierService.getSuppliersByCriteria(
                location, natureOfBusiness, manufacturingProcess, pageNo, pageSize, sortBy, sortDir
        ));
    }
}
