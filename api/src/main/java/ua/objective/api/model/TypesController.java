package ua.objective.api.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static ua.objective.api.common.Result.just;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.objective.api.common.Result;
import ua.objective.core.model.Model;
import ua.objective.core.model.beans.ModelBean;

import java.util.List;

@RestController
@RequestMapping("/model/types")
public class TypesController {

    @Autowired Model model;

    private ModelBean edit() {
        return (ModelBean) model;
    }

    @RequestMapping(method = GET)
    Result<List<TypeResp>> getTypes() {
        return just(model.getTypes().stream()
                .map(TypeResp::new)
                .collect(toList()));
    }

    @RequestMapping(method = POST)
    Result<TypeResp> addType(@RequestParam String group, @RequestParam String name) {
        return just(TypeResp.full(edit().createType(group, name)));
    }

    @RequestMapping(value = "/{qname:.*}", method = GET)
    Result<TypeResp> getType(@PathVariable String qname) {
        return just(TypeResp.full(model.getTypeByQName(qname)));
    }
}
