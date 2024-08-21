package com.makersharks.services;

import com.makersharks.dto.SupplierCreateDTO;
import com.makersharks.entities.Supplier;
import com.makersharks.entities.NatureOfBusiness;
import com.makersharks.entities.ManufacturingProcess;
import com.makersharks.exception.exception.ResourceNotFoundException;
import com.makersharks.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public Supplier createSupplier(SupplierCreateDTO supplierCreateDTO) {
        Supplier supplier = new Supplier();
        supplier.setCompanyName(supplierCreateDTO.getCompanyName());
        supplier.setWebsite(supplierCreateDTO.getWebsite());
        supplier.setLocation(supplierCreateDTO.getLocation());
        supplier.setNatureOfBusiness(NatureOfBusiness.valueOf(supplierCreateDTO.getNatureOfBusiness().toUpperCase()));
        supplier.setManufacturingProcesses(ManufacturingProcess.valueOf(supplierCreateDTO.getManufacturingProcesses().toUpperCase()));

        return supplierRepository.save(supplier);
    }

    public List<Supplier> getSuppliersByCriteria(String location, String natureOfBusiness, String manufacturingProcess,
                                                 int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        NatureOfBusiness natureOfBusinessEnum = NatureOfBusiness.valueOf(natureOfBusiness.toUpperCase());
        ManufacturingProcess manufacturingProcessEnum = ManufacturingProcess.valueOf(manufacturingProcess.toUpperCase());

        Page<Supplier> suppliersPage = supplierRepository.findByLocationAndNatureOfBusinessAndManufacturingProcesses(
                location, natureOfBusinessEnum, manufacturingProcessEnum, pageable);

        List<Supplier> supplierList = suppliersPage.getContent();

        if (!supplierList.isEmpty()) {
            return supplierList;
        } else {
            throw new ResourceNotFoundException("Supplier", "given location", location);
        }
    }
}
