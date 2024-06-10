package org.example.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.example.dao.EmployeeDAO;
import org.example.dao.JobDAO;
import org.example.dto.EmployeeFilterDto;
import org.example.dto.EmployeeIdDto;
import org.example.dto.EmployeesDto;
import org.example.dto.JobsDto;
import org.example.exceptions.DataNotFoundException;
import org.example.mappers.EmployeesMapper;
import org.example.mappers.JobsMapper;
import org.example.models.Employees;
import org.example.models.Jobs;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

@Path("/employees")
public class EmployeeController {

    @Context
    UriInfo uriInfo;

    @Context
    HttpHeaders headers;

    EmployeeDAO dao = new EmployeeDAO();

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "text/csv"})
    public Response getAllEmployees(@BeanParam EmployeeFilterDto filterDto) {
        try {
            GenericEntity<ArrayList<Employees>> employsList = new GenericEntity<ArrayList<Employees>>(dao.selectAllEmployees(filterDto)) {};
            if (headers.getAcceptableMediaTypes().contains(MediaType.APPLICATION_XML_TYPE)) {
                return Response.ok(employsList).type(MediaType.APPLICATION_XML).build();
            } else if (headers.getAcceptableMediaTypes().contains(MediaType.valueOf("text/csv"))) {
                return Response.ok(employsList).type("text/csv").build();
            }
            return Response.ok(employsList, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/{employee_id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, "text/csv"})
    public Response getEmployee(@PathParam("employee_id") int employeeId)throws SQLException {
        try {
            Employees employees = dao.selectEmployee(employeeId);
            if (employees == null) {
                throw new DataNotFoundException("Employee with ID " + employeeId + " not found");
            }
            Jobs j = JobDAO.selectJob(employees.getJob_id());

            EmployeesDto dto = EmployeesMapper.INSTANCE.toEmployeesDto(employees);
            addLink(dto);
            return Response.ok(dto).build();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @DELETE
    @Path("/{employee_id}")
    public Response deleteEmployee(@PathParam("employee_id") int employeeId) {
        try {
            dao.deleteEmployee(employeeId);
            return Response.noContent().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response insertEmployee(Employees employ) {
        try {
            dao.insertEmployee(employ);
            URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(employ.getEmployee_id())).build();
            return Response.created(uri).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PUT
    @Path("/{employee_id}")
    public Response updateEmployee(@PathParam("employee_id") int employeeId, Employees employ) {
        try {
            employ.setEmployee_id(employeeId);
            dao.updateEmployee(employ);
            return Response.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addLink(EmployeesDto dto) {
        URI selfUri = uriInfo.getAbsolutePath();
        dto.addLink(selfUri.toString(), "self");
    }
}