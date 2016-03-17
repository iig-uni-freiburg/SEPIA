package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts;

import java.util.List;

import de.invation.code.toval.misc.NamedComponent;

public interface IResourceContext extends NamedComponent {

        @Override
        public String getName();

        @Override
        public void setName(String name);

        public void blockResources(List<String> resources);

        public void unBlockResources(List<String> resources);

        public List<String> getAllowedResourcesFor(String activity);

        public List<String> getRandomAvailableResourceSetFor(String activity, boolean blockResources);

        public boolean isAvailable(String resourceName);

        public IResource getResourceObject(String resourceName);

        public boolean containsBlockedResources();

        public void reset();
        
        public void renameResource(String oldName, String newName);

}
